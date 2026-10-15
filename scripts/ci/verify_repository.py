#!/usr/bin/env python3
"""Small, dependency-free guard for repository governance and merge hygiene."""

from __future__ import annotations

import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
REQUIRED_FILES = (
    "README.md",
    "AGENTS.md",
    "docs/PROJECT_RULES.md",
    "docs/architecture/decisions/README.md",
)
TEXT_SUFFIXES = {
    ".md", ".yml", ".yaml", ".json", ".toml", ".py", ".ts", ".tsx",
    ".js", ".jsx", ".html", ".css", ".java", ".sql", ".sh", ".ps1",
    ".txt", ".xml", ".properties", ".gitignore",
}
MAX_FILE_BYTES = 1_000_000


def tracked_files() -> list[Path]:
    result = subprocess.run(
        ["git", "ls-files", "-z"],
        cwd=ROOT,
        check=True,
        stdout=subprocess.PIPE,
    )
    return [
        ROOT / item.decode("utf-8", errors="surrogateescape")
        for item in result.stdout.split(b"\0")
        if item
    ]


def main() -> int:
    errors: list[str] = []

    for relative in REQUIRED_FILES:
        path = ROOT / relative
        if not path.is_file():
            errors.append(f"Arquivo obrigatório ausente: {relative}")
        elif path.stat().st_size == 0:
            errors.append(f"Arquivo obrigatório vazio: {relative}")

    files = tracked_files()
    for path in files:
        if not path.is_file() or path.stat().st_size > MAX_FILE_BYTES:
            continue
        if path.suffix.lower() not in TEXT_SUFFIXES and path.name != ".gitignore":
            continue
        try:
            lines = path.read_text(encoding="utf-8").splitlines()
        except (UnicodeDecodeError, OSError):
            # Binary or unreadable files are outside this text-integrity check.
            continue

        in_conflict = False
        for number, line in enumerate(lines, start=1):
            if line.startswith(("<<<<<<< ", "||||||| ")):
                in_conflict = True
                errors.append(f"Possível marcador de conflito: {path.relative_to(ROOT)}:{number}")
            elif line.startswith(">>>>>>> "):
                in_conflict = False
                errors.append(f"Possível marcador de conflito: {path.relative_to(ROOT)}:{number}")

    if errors:
        print("Repository Guard: FALHOU")
        for error in errors:
            print(f" - {error}")
        return 1

    print("Repository Guard: OK")
    print(f" - {len(REQUIRED_FILES)} arquivos de governança obrigatórios presentes")
    print(" - nenhum marcador de conflito Git encontrado nos arquivos de texto rastreados")
    print(" - nenhuma dependência adicional instalada")
    return 0


if __name__ == "__main__":
    sys.exit(main())

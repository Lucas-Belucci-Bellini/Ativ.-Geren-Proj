# Sonora — interface web

Interface responsiva em Next.js + React para pesquisar faixas no SoundCloud e comparar letras/metadados usando LRCLIB e MusicBrainz.

## Requisitos
- Node.js 20.9 ou superior
- npm
- Token autorizado da API do SoundCloud para a busca externa

## Executar localmente

```bash
cd frontend
npm install
# Copie .env.example para .env.local e configure SOUNDCLOUD_ACCESS_TOKEN
npm run dev
```

Abra http://localhost:3000. O token fica no servidor nas rotas `/api/search`; não o coloque em variáveis `NEXT_PUBLIC_*` nem o envie ao navegador.

## Rotas
- `GET /api/search?q=...`: pesquisa faixas públicas no SoundCloud.
- `GET /api/verify?title=...&artist=...&duration=...`: consulta LRCLIB e MusicBrainz; uma fonte pode falhar sem apagar os resultados da outra.

A UI identifica correspondências aproximadas e não certifica autoria/direitos autorais. A reprodução de áudio não está implementada; o link abre a faixa na plataforma de origem. Capas e letras são exibidas apenas quando o provedor retorna esses dados; respeite os termos e direitos aplicáveis.

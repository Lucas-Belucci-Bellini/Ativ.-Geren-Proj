import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Sonora — descubra sua próxima música",
  description: "Pesquise faixas, confira artistas e encontre informações musicais.",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="pt-BR"><body>{children}</body></html>;
}

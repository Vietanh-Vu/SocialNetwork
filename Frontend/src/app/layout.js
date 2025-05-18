import { Inter } from "next/font/google";
import "./globals.css";
import { Toaster } from "@/components/ui/sonner";
import { ThemeProvider } from "next-themes";
import { AuthProvider } from "@/app/(overview)/components/context/AuthContext";
import { PostProvider } from "@/app/(overview)/components/context/PostContext";
import { CommentProvider } from "@/app/(overview)/components/context/CommentContext";
import { ReactionProvider } from "@/app/(overview)/components/context/ReactionContext";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "Social network",
  description: "Social network",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" suppressHydrationWarning={true}>
      <body className={inter.className} suppressHydrationWarning={true}>
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
          disableTransitionOnChange
        >
          <AuthProvider>
            <PostProvider>
              <CommentProvider>
                <ReactionProvider>{children}</ReactionProvider>
              </CommentProvider>
            </PostProvider>
          </AuthProvider>
          <Toaster position="top-center" />
        </ThemeProvider>
      </body>
    </html>
  );
}

"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Search } from "lucide-react"

export function GlobalConfigSearch() {
    const [searchCode, setSearchCode] = useState("")

    const handleSearch = (e) => {
        e.preventDefault()
        // Implement search functionality
        console.log("Searching for:", searchCode)
    }

    return (
        <form onSubmit={handleSearch} className="flex w-full max-w-sm items-center space-x-2">
            <Input
                type="text"
                placeholder="Search by config code"
                value={searchCode}
                onChange={(e) => setSearchCode(e.target.value)}
            />
            <Button type="submit" size="icon">
                <Search className="h-4 w-4" />
                <span className="sr-only">Search</span>
            </Button>
        </form>
    )
}

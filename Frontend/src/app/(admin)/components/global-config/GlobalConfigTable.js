"use client"

import { useState, useEffect } from "react"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Button } from "@/components/ui/button"
import { MoreHorizontal, Pencil, Trash, Search } from "lucide-react"
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
} from "@/components/ui/pagination"
import {EditGlobalConfigDialog} from "@/app/(admin)/components/global-config/EditGlobalConfigDialog";
import {DeleteGlobalConfigDialog} from "@/app/(admin)/components/global-config/DeleteGlobalConfigDialog";
import { getConfigs, getConfigByCode } from "@/lib/data";
import Spinner from "@/app/(admin)/ultils/Spinner";
import TextExpander from "@/app/(admin)/ultils/TextExpander";
import { Input } from "@/components/ui/input";

export function GlobalConfigTable() {
    const [page, setPage] = useState(1)
    const [editConfig, setEditConfig] = useState(null)
    const [deleteConfig, setDeleteConfig] = useState(null)
    const [configs, setConfigs] = useState([])
    const [pageMeta, setPageMeta] = useState({
        page: 1,
        pageSize: 10,
        totalElements: 0,
        totalPages: 1,
        hasPrev: false,
        hasNext: false
    })
    const [loading, setLoading] = useState(true)
    const [searchCode, setSearchCode] = useState("")
    const [searchResults, setSearchResults] = useState(null)
    const [searchLoading, setSearchLoading] = useState(false)

    useEffect(() => {
        const fetchConfigs = async () => {
            setLoading(true)
            const response = await getConfigs(page, pageMeta.pageSize)
            if (response.isSuccessful) {
                setConfigs(response.data.data)
                setPageMeta(response.data.pageMeta)
            }
            setLoading(false)
        }

        if (!searchResults) {
            fetchConfigs().then(r => {})
        }
    }, [page, pageMeta.pageSize, searchResults])

    const handleSearch = async (e) => {
        e.preventDefault()
        if (!searchCode.trim()) {
            setSearchResults(null)
            return
        }

        setSearchLoading(true)
        const response = await getConfigByCode(searchCode)
        if (response.isSuccessful) {
            if (response.data.data) {
                setSearchResults(response.data.data)
            } else {
                setSearchResults([])
            }
        } else {
            setSearchResults([])
        }
        setSearchLoading(false)
    }

    const clearSearch = () => {
        setSearchCode("")
        setSearchResults(null)
    }

    const displayedConfigs = searchResults || configs
    const isSearchMode = searchResults !== null

    return (
        <div className="space-y-4">
            <form onSubmit={handleSearch} className="flex w-full max-w-sm items-center space-x-2">
                <div className="flex-1">
                    <Input
                        placeholder="Search by config code..."
                        value={searchCode}
                        onChange={(e) => setSearchCode(e.target.value)}
                    />
                </div>
                <Button type="submit" disabled={searchLoading}>
                    {/*{searchLoading ? <Spinner size="sm" /> : <Search className="h-4 w-4 mr-2" />}*/}
                    Search
                </Button>
                {isSearchMode && (
                    <Button variant="outline" onClick={clearSearch}>
                        Clear Search
                    </Button>
                )}
            </form>

            <div className="rounded-md border">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Name</TableHead>
                            <TableHead>Code</TableHead>
                            <TableHead>Description</TableHead>
                            <TableHead className="w-[100px]">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {(loading && !isSearchMode) || searchLoading ? (
                            <TableRow>
                                <TableCell colSpan={4} className="text-center py-4">Loading...</TableCell>
                            </TableRow>
                        ) : displayedConfigs.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={4} className="text-center py-4">
                                    {isSearchMode ? "No configuration found with this code" : "Does not have any data"}
                                </TableCell>
                            </TableRow>
                        ) : (
                            displayedConfigs.map((config) => (
                                <TableRow key={config.id || `fallback-${idx}`}>
                                    <TableCell className="font-medium">{config.name}</TableCell>
                                    <TableCell className="font-mono text-sm">{config.code}</TableCell>
                                    <TableCell className="font-mono text-sm"><TextExpander>{config.desc}</TextExpander></TableCell>
                                    <TableCell>
                                        <DropdownMenu>
                                            <DropdownMenuTrigger asChild>
                                                <Button variant="ghost" className="h-8 w-8 p-0">
                                                    <span className="sr-only">Open menu</span>
                                                    <MoreHorizontal className="h-4 w-4" />
                                                </Button>
                                            </DropdownMenuTrigger>
                                            <DropdownMenuContent align="end">
                                                <DropdownMenuLabel>Actions</DropdownMenuLabel>
                                                <DropdownMenuSeparator />
                                                <DropdownMenuItem onClick={() => setEditConfig(config)}>
                                                    <Pencil className="mr-2 h-4 w-4" />
                                                    Edit
                                                </DropdownMenuItem>
                                                <DropdownMenuItem
                                                    onClick={() => setDeleteConfig(config)}
                                                    className="text-destructive focus:text-destructive"
                                                >
                                                    <Trash className="mr-2 h-4 w-4" />
                                                    Delete
                                                </DropdownMenuItem>
                                            </DropdownMenuContent>
                                        </DropdownMenu>
                                    </TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </div>

            {!isSearchMode && (
                <Pagination>
                    <PaginationContent>
                        <PaginationItem>
                            <PaginationPrevious
                                onClick={() => setPage((p) => Math.max(1, p - 1))}
                                className={!pageMeta.hasPrev ? "pointer-events-none opacity-50" : ""}
                            />
                        </PaginationItem>

                        {Array.from({ length: pageMeta.totalPages }).map((_, i) => (
                            <PaginationItem key={i}>
                                <PaginationLink onClick={() => setPage(i + 1)} isActive={page === i + 1}>
                                    {i + 1}
                                </PaginationLink>
                            </PaginationItem>
                        ))}

                        <PaginationItem>
                            <PaginationNext
                                onClick={() => setPage((p) => Math.min(pageMeta.totalPages, p + 1))}
                                className={!pageMeta.hasNext ? "pointer-events-none opacity-50" : ""}
                            />
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
            )}

            {editConfig && (
                <EditGlobalConfigDialog
                    config={editConfig}
                    open={!!editConfig}
                    onOpenChange={(open) => !open && setEditConfig(null)}
                />
            )}

            {deleteConfig && (
                <DeleteGlobalConfigDialog
                    config={deleteConfig}
                    open={!!deleteConfig}
                    onOpenChange={(open) => !open && setDeleteConfig(null)}
                />
            )}
        </div>
    )
}
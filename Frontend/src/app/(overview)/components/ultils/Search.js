"use client";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {usePathname, useRouter, useSearchParams} from "next/navigation";
import {useDebouncedCallback} from "use-debounce";

function Search({searchFor}) {
    const searchParams = useSearchParams();
    const pathname = usePathname();
    const {replace} = useRouter();

    const handleSearch = useDebouncedCallback((value) => {
        const params = new URLSearchParams(searchParams);
        if (value) {
            params.set('query', value);
        } else {
            params.delete('query');
        }
        replace(`${pathname}?${params.toString()}`);
    }, 500)

    return (
        <form className="flex items-center gap-4">
            <Input
                type="search"
                placeholder={`Search for ${searchFor} ...`}
                className="flex-1"
                onChange={(e) => {
                    handleSearch(e.target.value)
                }}
                defaultValue={searchParams.get('query')?.toString()}
            />
            {/*<Button type="submit">Search</Button>*/}
        </form>
    )
}

export default Search;
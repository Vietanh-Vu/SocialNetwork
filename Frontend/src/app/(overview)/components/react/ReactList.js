"use client"

import {useEffect, useState, useCallback} from "react";
import {usePathname, useRouter, useSearchParams} from "next/navigation";
import {useReaction} from "@/app/(overview)/components/context/ReactionContext";
import ReactItem from "@/app/(overview)/components/react/ReactItem";
import Spinner from "@/app/(overview)/components/ultils/Spinner";

function ReactionList({initialReactions, initialPageMeta}) {
    const {reactions, setReactions} = useReaction();
    const [pageMeta, setPageMeta] = useState(initialPageMeta);
    const [isLoading, setIsLoading] = useState(false);
    const searchParams = useSearchParams();
    const pathname = usePathname();
    const {replace} = useRouter();

    useEffect(() => {
        setReactions(prevReactions => {
            const newReactionIds = new Set(initialReactions.map(reaction => reaction.id));
            const uniquePrevReactions = prevReactions.filter(reaction => !newReactionIds.has(reaction.id));
            return [...uniquePrevReactions, ...initialReactions];
        });
        setPageMeta(initialPageMeta);
    }, [initialReactions, initialPageMeta, setReactions]);

    const loadMore = useCallback(async () => {
        if (pageMeta.hasNext && !isLoading) {
            setIsLoading(true);
            const params = new URLSearchParams(searchParams);
            params.set("page", (pageMeta.page + 1).toString());
            await replace(`${pathname}?${params.toString()}`);
            setIsLoading(false);
        }
    }, [pageMeta, searchParams, pathname, replace, isLoading]);

    return (
        <>
            <div>
                {reactions.map(reaction => (
                    <ReactItem key={reaction.id} reactInfo={reaction}/>
                ))}
            </div>
            {pageMeta.hasNext && (
                <div className="text-center mt-4">
                    <button
                        onClick={loadMore}
                        className="text-primary font-semibold hover:underline"
                    >
                        Load more
                    </button>
                </div>
            )}
            {isLoading && (
                <div className="mt-4 text-center">
                    <Spinner/>
                </div>
            )}
        </>
    );
}

export default ReactionList;
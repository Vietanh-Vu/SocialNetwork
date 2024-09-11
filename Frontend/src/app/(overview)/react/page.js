import ReactItem from "@/app/(overview)/components/react/ReactItem";
import CloseDialogPage from "@/app/(overview)/components/ultils/CloseDialogPage";
import React from "react";
import {getReactionInPost} from "@/lib/data";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import {ScrollArea} from "@/components/ui/scroll-area";
import ReactList from "@/app/(overview)/components/react/ReactList";

export default async function Page({searchParams}) {
    console.log(searchParams.postId)
    const result = await getReactionInPost(searchParams?.page, searchParams.postId);

    let pageMeta = null;
    let reactions = null;

    if (result.isSuccessful) {
        pageMeta = result.data.pageMeta;
        reactions = result.data.data;
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
            <div className="bg-background p-6 rounded-lg shadow-lg w-full max-w-3xl flex flex-col h-[80vh]">
                <h2 className="text-xl font-bold mb-4">Reactions</h2>
                {!result.isSuccessful ? <SomethingWentWrong/> : (
                    <ScrollArea className="flex-grow">
                        {!result.isSuccessful ? <SomethingWentWrong/> : (
                            <ReactList initialReactions={reactions} initialPageMeta={pageMeta}/>
                        )}
                    </ScrollArea>
                )}
                <div className="mt-6">
                    <div className="flex justify-end mt-2">
                        <CloseDialogPage>Button</CloseDialogPage>
                    </div>
                </div>
            </div>
        </div>
    )
}
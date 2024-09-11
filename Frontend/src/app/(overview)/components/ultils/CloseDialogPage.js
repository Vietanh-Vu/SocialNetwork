"use client";

import {useRouter} from "next/navigation";
import {Button} from "@/components/ui/button";
import {useComment} from "@/app/(overview)/components/context/CommentContext";
import {useReaction} from "@/app/(overview)/components/context/ReactionContext";

function CloseDialogPage() {
    const router = useRouter();
    const {setComments} = useComment();
    const {setReactions} = useReaction();

    const handleClose = async () => {
        setComments([]);
        setReactions([]);
        router.back();
    };

    return (
        <Button onClick={handleClose}>
            Close
        </Button>
    );
}

export default CloseDialogPage;
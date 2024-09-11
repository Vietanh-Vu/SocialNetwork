import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from "@/components/ui/dropdown-menu";
import {useState} from "react";
import {Ellipsis} from "lucide-react";
import {Button} from "@/components/ui/button";
import {deleteComment} from "@/lib/action";
import {toast} from "sonner";
import EditCommentForm from "@/app/(overview)/components/comment/EditCommentForm";
import {
    Dialog,
    DialogContent,
    DialogTrigger,
    DialogTitle,
    DialogDescription,
    DialogFooter,
} from "@/components/ui/dialog";
import {useRouter} from "next/navigation";

function MoreActionComment({commentInfo}) {
    const [showActions, setShowActions] = useState(false);
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const router = useRouter();

    const handleDeleteComment = async () => {
        const result = await deleteComment(commentInfo.commentId);
        if (result.isSuccessful) {
            toast.success("Comment deleted successfully");
            router.refresh()
            setShowDeleteConfirm(false);
        } else {
            console.log(result.message);
            toast.error("Error while deleting comment");
        }
    };

    return (
        <>
            <Dialog>
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button onClick={() => setShowActions(!showActions)} variant="ghost">
                            <Ellipsis/>
                        </Button>
                    </DropdownMenuTrigger>
                    {showActions && (
                        <DropdownMenuContent align="end" className="w-40">
                            <DialogTrigger asChild>
                                <DropdownMenuItem>
                                    <Button
                                        className="w-full rounded-md bg-background text-foreground hover:bg-muted hover:text-foreground-muted"
                                    >
                                        Edit
                                    </Button>
                                </DropdownMenuItem>
                            </DialogTrigger>
                            <DropdownMenuItem onSelect={() => setShowDeleteConfirm(true)}>
                                <Button
                                    className="mt-2 w-full rounded-md bg-background text-foreground hover:bg-muted hover:text-foreground-muted"
                                >
                                    Delete
                                </Button>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    )}
                </DropdownMenu>
                <DialogContent>
                    <EditCommentForm commentInfo={commentInfo}/>
                </DialogContent>
            </Dialog>

            <Dialog open={showDeleteConfirm} onOpenChange={setShowDeleteConfirm}>
                <DialogContent>
                    <DialogTitle>Confirm Delete</DialogTitle>
                    <DialogDescription>
                        Are you sure you want to delete this comment? This action cannot be undone.
                    </DialogDescription>
                    <DialogFooter>
                        <Button variant="outline" onClick={() => setShowDeleteConfirm(false)}>
                            Cancel
                        </Button>
                        <Button variant="destructive" onClick={handleDeleteComment}>
                            Delete
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </>
    );
}

export default MoreActionComment;
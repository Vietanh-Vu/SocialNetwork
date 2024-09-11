import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from "@/components/ui/dropdown-menu";
import {useState} from "react";
import {Ellipsis} from "lucide-react";
import {Button} from "@/components/ui/button";
import {deletePost} from "@/lib/action";
import {toast} from "sonner";
import {
    Dialog,
    DialogContent,
    DialogTrigger,
    DialogTitle,
    DialogDescription,
    DialogFooter,
} from "@/components/ui/dialog";
import EditPostForm from "@/app/(overview)/components/post/EditPostForm";
import {useRouter} from "next/navigation";

function MoreActionPost({postInfo}) {
    const [showActions, setShowActions] = useState(false);
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const router = useRouter();

    const handleDeletePost = async () => {
        const result = await deletePost(postInfo.id);
        if (result.isSuccessful) {
            toast.success("Post deleted successfully");
            setShowDeleteConfirm(false);
        } else {
            toast.error("Error while deleting post");
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
                <DialogContent className="sm:max-w-[650px]">
                    <EditPostForm postInfo={postInfo}/>
                </DialogContent>
            </Dialog>

            <Dialog open={showDeleteConfirm} onOpenChange={setShowDeleteConfirm}>
                <DialogContent>
                    <DialogTitle>Confirm Delete</DialogTitle>
                    <DialogDescription>
                        Are you sure you want to delete this post? This action cannot be undone.
                    </DialogDescription>
                    <DialogFooter>
                        <Button variant="outline" onClick={() => setShowDeleteConfirm(false)}>
                            Cancel
                        </Button>
                        <Button variant="destructive" onClick={handleDeletePost}>
                            Delete
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </>
    );
}

export default MoreActionPost;
import {Button} from "@/components/ui/button";
import {toast} from "sonner";
import {deleteRequestFriend} from "@/lib/action";
import {useRouter} from "next/navigation";

function DeleteRequestButton({userId}) {
    const router = useRouter();

    async function handleDeleteRequest() {
        const result = await deleteRequestFriend(userId);
        if (result.isSuccessful) {
            toast.success("Request has been deleted");
            router.refresh()
        } else {
            console.log(result.message);
            toast.error("There was an error, please try again later");
        }
    }

    return (
        <Button variant="outline" size="sm" onClick={handleDeleteRequest}>
            Delete
        </Button>
    )
}

export default DeleteRequestButton;
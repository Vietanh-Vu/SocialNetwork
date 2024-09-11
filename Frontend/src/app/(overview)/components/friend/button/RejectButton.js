import {Button} from "@/components/ui/button";
import {rejectRequestFriend} from "@/lib/action";
import {toast} from "sonner";
import {useRouter} from "next/navigation";

function RejectButton({userId}) {
    const router = useRouter();

    async function handleRejectRequest() {
        const result = await rejectRequestFriend(userId);
        if (result.isSuccessful) {
            toast.success("Request has been canceled");
            router.refresh()
        } else {
            toast.error("There was an error, please try again later");
        }
    }

    return (
        <Button variant="outline" size="sm" onClick={handleRejectRequest}>
            Reject
        </Button>
    )
}

export default RejectButton;
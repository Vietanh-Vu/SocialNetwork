import {Button} from "@/components/ui/button";
import {sendRequestFriend} from "@/lib/action";
import {toast} from "sonner";
import {useRouter} from "next/navigation";

function SendRequestButton({userId}) {
    const router = useRouter();

    async function handleSendRequest() {
        const result = await sendRequestFriend(userId);
        if (result.isSuccessful) {
            toast.success("Request has been sent");
            router.refresh()
        } else {
            toast.error("There was an error, please try again later");
        }
    }

    return (
        <Button variant="outline" size="sm" onClick={handleSendRequest}>
            Send
        </Button>
    )
}

export default SendRequestButton;
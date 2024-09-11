import {Button} from "@/components/ui/button";
import {unFriend} from "@/lib/action";
import {toast} from "sonner";
import {useRouter} from "next/navigation";

function UnFriendButton({userId}) {
    const router = useRouter();

    async function handleUnFriend() {
        const result = await unFriend(userId);
        if (result.isSuccessful) {
            toast.success("Unfriend successfully");
            router.refresh();
        } else {
            toast.error("Unfriend failed");
        }
    }

    return (
        <Button variant="outline" size="sm" onClick={handleUnFriend}>
            Unfriend
        </Button>
    )
}

export default UnFriendButton;
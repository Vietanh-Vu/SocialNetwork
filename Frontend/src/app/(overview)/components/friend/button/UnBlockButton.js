import {Button} from "@/components/ui/button";
import {unBlock} from "@/lib/action";
import {toast} from "sonner";
import {useRouter} from "next/navigation";

function UnBlockButton({userId}) {
    const router = useRouter();

    async function handleUnBlock() {
        const result = await unBlock(userId);
        if (result.isSuccessful) {
            toast.success("Unblock successfully");
            router.refresh()
        } else {
            console.log(result.message);
            toast.error("Something went wrong");
        }
    }

    return (
        <Button variant="outline" size="sm" onClick={handleUnBlock}>
            Unblock
        </Button>
    )
}

export default UnBlockButton;
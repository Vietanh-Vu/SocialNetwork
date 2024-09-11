import {redirect} from "next/navigation";
import {getUserProfile} from "@/lib/data";

export default async function Page() {
    const {_, data: userInfo} = await getUserProfile();

    if (!userInfo?.id) {
        redirect("/login");
    }

    redirect(`/home/${userInfo.id}`);
}
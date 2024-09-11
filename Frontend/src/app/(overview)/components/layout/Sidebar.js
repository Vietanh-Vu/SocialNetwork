import UserCard from "@/app/(overview)/components/user/UserCard";
import SuggestFriendList from "@/app/(overview)/components/friend/suggest/SuggestFriendList";
import PostForm from "@/app/(overview)/components/post/PostForm";

function Sidebar() {
    return (
        <>
            <UserCard/>
            <SuggestFriendList className="max-h-24"/>
            <PostForm/>
        </>
    )
}

export default Sidebar;
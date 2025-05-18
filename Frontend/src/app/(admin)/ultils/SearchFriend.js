"use client"
import {useState} from 'react';
import {useDebouncedCallback} from "use-debounce";
import {searchFriend, searchUser} from "@/lib/data";
import {toast} from "sonner";
import {Input} from "@/components/ui/input";
import {SearchIcon} from "lucide-react";
import {ScrollArea} from "@/components/ui/scroll-area";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {getAvatarFallback} from "@/lib/utils";

function SearchFriend({onUserClick}) {
    const [users, setUsers] = useState([]);
    const [value, setValue] = useState('');

    const handleSearch = useDebouncedCallback(async (inputValue) => {
        setValue(inputValue);
        const result = await searchFriend(1, inputValue);
        if (!result.isSuccessful) {
            console.log(result.message);
            toast.error("Failed to search users");
        }
        setUsers(result.data.data);
    }, 300);

    const handleUserClick = (user) => {
        if (onUserClick) {
            onUserClick(user);
        }
    };

    return (
        <div className="flex flex-col gap-4">
            <div className="relative">
                <Input
                    type="search"
                    placeholder="Search users..."
                    value={value}
                    onChange={(e) => handleSearch(e.target.value)}
                    className="pr-10"
                />
                <SearchIcon className="absolute right-3 top-3 h-4 w-4 text-muted-foreground"/>
            </div>
            <ScrollArea className="h-36 rounded-md border">
                <div className="p-4 grid gap-4">
                    {users.map((user) => (
                        <div key={user.id} className="flex items-center gap-4 hover:cursor-pointer"
                             onClick={() => handleUserClick(user)}>
                            <Avatar>
                                <AvatarImage src={user.avatar} alt={user.username}/>
                                <AvatarFallback>{getAvatarFallback(user.username)}</AvatarFallback>
                            </Avatar>
                            <div className="text-sm font-medium">{user.username}</div>
                        </div>
                    ))}
                </div>
            </ScrollArea>
        </div>
    )
}

export default SearchFriend;
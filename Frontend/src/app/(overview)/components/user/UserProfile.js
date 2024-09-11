"use client";
import {Avatar, AvatarImage, AvatarFallback} from "@/components/ui/avatar"
import {capitalizeFirstLetter, getAvatarFallback} from "@/lib/utils";
import Image from "next/image";
import {Briefcase, Calendar, Dna, Earth, FilePen, FolderLock, GraduationCap, Mail, MapPinned, User} from "lucide-react";
import {Button} from "@/components/ui/button";
import {useAuth} from "@/app/(overview)/components/context/AuthContext";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import ProfileForm from "@/app/(overview)/components/ProfileForm";

function UserProfile({userProfile}) {
    const {currentUserId, loading} = useAuth();
    if (loading) {
        return <Spinner/>
    }
    return (
        <div className="w-full max-w-5xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
            <div className="relative h-48 sm:h-64 md:h-80 lg:h-96 overflow-hidden rounded-lg">
                <Image
                    src={userProfile.backgroundImage ? userProfile.backgroundImage : "/black.jpg"}
                    alt="Background"
                    width={1920}
                    height={1080}
                    className="w-full h-full object-cover"
                    style={{aspectRatio: "1920/1080", objectFit: "cover"}}
                />
                <div className="absolute inset-0 bg-gradient-to-t from-muted to-transparent"/>
                <div className="absolute bottom-4 left-4 sm:bottom-6 sm:left-6 md:bottom-8 md:left-8">
                    <Avatar className="w-16 h-16 sm:w-20 sm:h-20 md:w-24 md:h-24 border-4 border-background">
                        <AvatarImage src={userProfile.avatar} alt="User Avatar"/>
                        <AvatarFallback>{getAvatarFallback(userProfile.username)}</AvatarFallback>
                    </Avatar>
                </div>
            </div>
            <div className="mt-8 sm:mt-12 md:mt-16 lg:mt-20 grid gap-6">
                <div className="flex items-center justify-between">
                    <div className="grid gap-1">
                        <h1 className="text-2xl sm:text-3xl md:text-4xl font-bold">{userProfile.username}</h1>
                    </div>
                    {currentUserId === userProfile.id && (
                        // <Button variant="outline" size="sm">
                        //     <FilePen className="w-4 h-4 mr-2"/>
                        //     Edit
                        // </Button>
                        <ProfileForm userProfile={userProfile}/>
                    )}
                </div>
                <div className="flex items-center gap-2">
                    {userProfile.visibility === "PUBLIC" && <Earth className="w-5 h-5 text-muted-foreground"/>}
                    {userProfile.visibility === "PRIVATE" &&
                        <FolderLock className="w-5 h-5 text-muted-foreground"/>}
                    {userProfile.visibility === "FRIEND" &&
                        <User className="w-5 h-5 text-muted-foreground"/>}
                    <span>{capitalizeFirstLetter(userProfile.visibility)}</span>
                </div>
                <div className="grid sm:grid-cols-2 gap-6">
                    <div className="grid gap-2">
                        <div className="flex items-center gap-2">
                            <Mail className="w-5 h-5 text-muted-foreground"/>
                            <span>{userProfile.email}</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <Dna className="w-5 h-5 text-muted-foreground"/>
                            <span>{capitalizeFirstLetter(userProfile.gender)}</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <MapPinned className="w-5 h-5 text-muted-foreground"/>
                            <span>{userProfile.location}</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <Calendar className="w-5 h-5 text-muted-foreground"/>
                            <span>{userProfile.dateOfBirth}</span>
                        </div>
                    </div>
                    <div className="grid gap-2">
                        <div className="flex items-center gap-2">
                            <Briefcase className="w-5 h-5 text-muted-foreground"/>
                            <span>{userProfile.work}</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <GraduationCap className="w-5 h-5 text-muted-foreground"/>
                            <span>{userProfile.education}</span>
                        </div>
                    </div>
                </div>
                <div className="grid gap-2">
                    <h2 className="text-xl font-semibold">Bio</h2>
                    <p className="text-muted-foreground">
                        {userProfile.bio}
                    </p>
                </div>
            </div>
        </div>
    )
}

export default UserProfile
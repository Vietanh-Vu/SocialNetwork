"use client"
import {Button} from "@/components/ui/button"
import {Label} from "@/components/ui/label"
import {Input} from "@/components/ui/input"
import {Textarea} from "@/components/ui/textarea"
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {Tabs, TabsContent} from "@/components/ui/tabs"
import Image from "next/image";
import NotAllow from "@/app/(overview)/components/ultils/NotAllow";
import {getAvatarFallback} from "@/lib/utils";

function UserForm({userProfile}) {
    return (
        <div className="w-full max-w-4xl mx-auto mt-4">
            <div className="rounded-lg overflow-hidden">
                <div className="relative h-48 sm:h-64 md:h-80">
                    <Image
                        src={userProfile.backgroundImage ? userProfile.backgroundImage : "/black.jpg"}
                        alt="Background"
                        className="object-cover w-full h-full"
                        width="800"
                        height="320"
                        style={{aspectRatio: "800/320", objectFit: "cover"}}
                    />
                    <div
                        className="absolute bottom-0 left-0 right-0 p-4 bg-gradient-to-t from-muted to-transparent">
                        <div className="flex items-center gap-4">
                            <Avatar className="w-20 h-20 md:w-24 md:h-24 border-4 border-background">
                                <AvatarImage src={userProfile.avatar} alt="User Avatar"/>
                                <AvatarFallback>{getAvatarFallback(userProfile.username)}</AvatarFallback>
                            </Avatar>
                            <div className="flex-1">
                                <h2 className="text-2xl font-bold">{userProfile.username}</h2>
                            </div>
                            <Button variant="outline">Edit</Button>
                        </div>
                    </div>
                </div>
                <div className="p-6 md:p-8">
                    <Tabs>
                        <TabsContent>
                            <div className="grid gap-6">
                                <div className="grid sm:grid-cols-2 gap-4">
                                    <div className="grid gap-1">
                                        <Label htmlFor="firstName">First Name</Label>
                                        <Input id="firstName" defaultValue={userProfile.firstName} disabled/>
                                    </div>
                                    <div className="grid gap-1">
                                        <Label htmlFor="lastName">Last Name</Label>
                                        <Input id="lastName" defaultValue={userProfile.lastName} disabled/>
                                    </div>
                                </div>
                                <div className="grid sm:grid-cols-2 gap-4">
                                    <div className="grid gap-1">
                                        <Label htmlFor="email">Email</Label>
                                        <Input id="email" type="email" defaultValue={userProfile.email} disabled/>
                                    </div>
                                    <div className="grid gap-1">
                                        <Label htmlFor="gender">Gender</Label>
                                        <Input id="email" type="email" defaultValue={userProfile.gender} disabled/>
                                    </div>
                                </div>
                                <div className="grid gap-1">
                                    <Label htmlFor="bio">Bio</Label>
                                    <Textarea id="bio" rows={3}
                                              defaultValue={userProfile.bio} disabled/>
                                </div>
                                <div className="grid sm:grid-cols-2 gap-4">
                                    <div className="grid gap-1">
                                        <Label htmlFor="location">Location</Label>
                                        <Input id="location" defaultValue={userProfile.location} disabled/>
                                    </div>
                                    <div className="grid gap-1">
                                        <Label htmlFor="dob">Date of Birth</Label>
                                        <Input id="dob" type="date" defaultValue={userProfile.dateOfBirth} disabled/>
                                    </div>
                                </div>
                            </div>
                        </TabsContent>
                        <TabsContent>
                            <div className="grid gap-4">
                                <div className="grid gap-1">
                                    <Label htmlFor="work">Work</Label>
                                    <Textarea id="work" rows={3} defaultValue={userProfile.work}
                                              disabled/>
                                </div>
                            </div>
                        </TabsContent>
                        <TabsContent>
                            <div className="grid gap-4">
                                <div className="grid gap-1">
                                    <Label htmlFor="education">Education</Label>
                                    <Textarea
                                        id="education"
                                        rows={3}
                                        defaultValue={userProfile.education}
                                        disabled
                                    />
                                </div>
                            </div>
                        </TabsContent>
                    </Tabs>
                </div>
            </div>
        </div>
    )
}

export default UserForm;
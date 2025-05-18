"use client";

import { useState } from "react";
import Image from "next/image";
import {
    Dialog,
    DialogContent,
    DialogClose,
} from "@/components/ui/dialog";
import { X } from "lucide-react";

function ImageViewer({ src, alt, width, height, className }) {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <>
            <Image
                src={src}
                alt={alt}
                width={width}
                height={height}
                className={`cursor-pointer ${className}`}
                onClick={() => setIsOpen(true)}
            />

            <Dialog open={isOpen} onOpenChange={setIsOpen}>
                <DialogContent className="max-w-[90vw] max-h-[90vh] p-0 border-none bg-transparent">
                    <div className="relative w-full h-full flex items-center justify-center">
                        <DialogClose className="absolute right-2 top-2 z-50 bg-black/50 rounded-full p-2 text-white hover:bg-black/70">
                            <X className="h-6 w-6" />
                        </DialogClose>
                        <Image
                            src={src}
                            alt={alt}
                            width={1500}
                            height={1000}
                            className="max-h-[90vh] max-w-[90vw] object-contain"
                            priority
                        />
                    </div>
                </DialogContent>
            </Dialog>
        </>
    );
}

export default ImageViewer;
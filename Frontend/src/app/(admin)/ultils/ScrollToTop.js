"use client";

import React, {useState, useEffect} from 'react';
import {ArrowUp} from 'lucide-react';
import {Button} from "@/components/ui/button";

const ScrollToTop = () => {
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const toggleVisibility = () => {
            if (window.pageYOffset > 300) {
                setIsVisible(true);
            } else {
                setIsVisible(false);
            }
        };

        window.addEventListener("scroll", toggleVisibility);

        return () => window.removeEventListener("scroll", toggleVisibility);
    }, []);

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    };

    return (
        <>
            {isVisible && (
                <Button
                    onClick={scrollToTop}
                    className="fixed bottom-4 right-4 p-2 rounded-full shadow-lg transition-all duration-300 z-50"
                    aria-label="Scroll to top"
                >
                    <ArrowUp size={24}/>
                </Button>
            )}
        </>
    );
};

export default ScrollToTop;
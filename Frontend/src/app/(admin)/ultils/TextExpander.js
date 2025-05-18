"use client";

import {useState} from "react";

function TextExpander({children}) {
    const [isExpanded, setIsExpanded] = useState(false);
    if (!children) {
        return <span></span>;
    }

    if (children.split(" ").length < 40) {
        return <span>{children}</span>;
    }

    const displayText = isExpanded
        ? children
        : children.split(" ").slice(0, 40).join(" ") + "...";

    return (
        <span>
            {displayText}
            <button
                className="border-b border-primary-700 leading-3 pb-1"
                onClick={() => setIsExpanded(!isExpanded)}
            >
            {isExpanded ? "Show less" : "Show more"}
      </button>
    </span>
    );
}

export default TextExpander;

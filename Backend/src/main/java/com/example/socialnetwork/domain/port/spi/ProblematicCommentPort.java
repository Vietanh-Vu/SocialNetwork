package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.ProblematicCommentDomain;

public interface ProblematicCommentPort {
    void createProblematicComment(ProblematicCommentDomain comment);
}

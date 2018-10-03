package com.imageorvideosilde;

import java.io.Serializable;

public class bennerlistdat implements Serializable {
    private String name;
    private String urrl;
    private String evenid;


    public bennerlistdat() {
    }

    public bennerlistdat(String name, String urrl,String evenid) {
        this.name = name;
        this.urrl = urrl;
        this.evenid = evenid;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrrl(String urrl) {
        this.urrl = urrl;
    }

    public String getUrrl() {
        return urrl;
    }

    public void setEvenid(String evenid) {
        this.evenid = evenid;
    }

    public String getEvenid() {
        return evenid;
    }
}


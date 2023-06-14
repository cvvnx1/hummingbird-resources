package com.ganwhat.hummingbird.resources.drools;

/**
 * @author cvvnx1@hotmail.com
 * @since Jun 09, 2023
 */
public class _Hello {
    private String world;
    private InnerHello innerHello;

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public InnerHello getInnerHello() {
        return innerHello;
    }

    public void setInnerHello(InnerHello innerHello) {
        this.innerHello = innerHello;
    }

    public static class InnerHello {
        private String innerWorld;
        private Integer innerInt;

        public String getInnerWorld() {
            return innerWorld;
        }

        public void setInnerWorld(String innerWorld) {
            this.innerWorld = innerWorld;
        }

        public Integer getInnerInt() {
            return innerInt;
        }

        public void setInnerInt(Integer innerInt) {
            this.innerInt = innerInt;
        }
    }
}

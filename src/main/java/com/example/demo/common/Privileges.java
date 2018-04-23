package com.example.demo.common;

import com.example.demo.controller.BookingController;
import com.example.demo.exception.RoleNotFoundException;

public class  Privileges
{
    public enum Privilege {
        NoPriv,
        Priv1,
        Priv2,
        Priv3
    }

    public static Privilege convertStringToPrivelege(String privilege)
    {
        switch (privilege.toLowerCase()) {
            case "priv1":
                return Privilege.Priv1;
            case "priv2":
                return Privilege.Priv2;
            case "priv3":
                return Privilege.Priv3;
        }
        return Privilege.NoPriv;
    }

}

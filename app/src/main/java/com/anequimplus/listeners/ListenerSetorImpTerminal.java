package com.anequimplus.listeners;

import com.anequimplus.entity.SetorImpTerminal;

import java.util.List;

public interface ListenerSetorImpTerminal {
    void ok(List<SetorImpTerminal> l) ;
    void erro(String msg);
}

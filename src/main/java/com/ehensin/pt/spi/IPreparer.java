package com.ehensin.pt.spi;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.ExeResult;

public interface IPreparer {
    public ExeResult prepare()throws ExeException;
}

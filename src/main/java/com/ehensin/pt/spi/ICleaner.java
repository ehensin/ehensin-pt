package com.ehensin.pt.spi;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.ExeResult;

public interface ICleaner {
    public ExeResult clean() throws ExeException;
}

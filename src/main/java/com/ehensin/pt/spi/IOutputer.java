package com.ehensin.pt.spi;

import com.ehensin.pt.ExeException;

public interface IOutputer {
    public void print(String msg) throws ExeException;
}

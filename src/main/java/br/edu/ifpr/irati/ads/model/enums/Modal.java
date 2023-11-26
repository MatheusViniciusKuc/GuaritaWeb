package br.edu.ifpr.irati.ads.model.enums;

import java.io.Serializable;

public enum Modal implements Serializable{

    ENTREGA_CHAVE_SERVIDOR,
    ENTREGA_CHAVE_VIGILANTE,
    REGISTRAR_OCORRENCIA,
    CANCELAR_EMPRESTIMO,
    LIBERAR_EMPRESTIMO,
    EXCLUIR;
}

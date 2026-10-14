package br.edu.uniesp.assistencia.internal.ordemservico.mapper;

import br.edu.uniesp.assistencia.internal.ordemservico.dto.CriarOrdemServicoResponse;
import br.edu.uniesp.assistencia.internal.ordemservico.entity.OrdemServicoEntity;

public final class OrdemServicoMapper {

    private OrdemServicoMapper() {
    }

    public static CriarOrdemServicoResponse paraResposta(OrdemServicoEntity ordemServico) {
        return new CriarOrdemServicoResponse(
                ordemServico.getId(),
                ordemServico.getNumeroProtocolo(),
                ordemServico.getStatus().name(),
                ordemServico.getDataAbertura()
        );
    }
}

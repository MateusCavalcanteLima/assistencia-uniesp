package br.edu.uniesp.assistencia.internal.ordemservico.dto;

import java.time.LocalDateTime;

public record CriarOrdemServicoResponse(
        Long id,
        String numeroProtocolo,
        String status,
        LocalDateTime dataAbertura
) {
}

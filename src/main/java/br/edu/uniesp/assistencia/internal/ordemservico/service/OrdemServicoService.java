package br.edu.uniesp.assistencia.internal.ordemservico.service;

import br.edu.uniesp.assistencia.internal.cliente.entity.ClienteEntity;
import br.edu.uniesp.assistencia.internal.cliente.repository.ClienteRepository;
import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import br.edu.uniesp.assistencia.internal.equipamento.repository.EquipamentoRepository;
import br.edu.uniesp.assistencia.internal.ordemservico.dto.CriarOrdemServicoRequest;
import br.edu.uniesp.assistencia.internal.ordemservico.dto.CriarOrdemServicoResponse;
import br.edu.uniesp.assistencia.internal.ordemservico.entity.HistoricoStatusEntity;
import br.edu.uniesp.assistencia.internal.ordemservico.entity.OrdemServicoEntity;
import br.edu.uniesp.assistencia.internal.ordemservico.entity.StatusOrdemServico;
import br.edu.uniesp.assistencia.internal.ordemservico.mapper.OrdemServicoMapper;
import br.edu.uniesp.assistencia.internal.ordemservico.repository.OrdemServicoRepository;
import br.edu.uniesp.assistencia.shared.exception.RecursoNaoEncontradoException;
import br.edu.uniesp.assistencia.shared.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private static final DateTimeFormatter FORMATTER_DATA = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String RESPONSAVEL_SISTEMA = "SISTEMA";

    private final ClienteRepository clienteRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    @Transactional
    public CriarOrdemServicoResponse abrirOrdemServico(CriarOrdemServicoRequest request) {
        ClienteEntity cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", request.clienteId()));

        if (!cliente.isAtivo()) {
            throw new RegraNegocioException(
                    "Não é permitido abrir Ordem de Serviço para um cliente inativo.");
        }

        EquipamentoEntity equipamento = equipamentoRepository.findById(request.equipamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipamento", request.equipamentoId()));

        if (!equipamento.getCliente().getId().equals(cliente.getId())) {
            throw new RegraNegocioException(
                    "O equipamento informado não pertence ao cliente informado.");
        }

        LocalDateTime dataAbertura = LocalDateTime.now();
        String numeroProtocolo = gerarNumeroProtocolo(dataAbertura);

        OrdemServicoEntity ordemServico = new OrdemServicoEntity(
                numeroProtocolo,
                request.descricaoProblema(),
                cliente,
                equipamento,
                dataAbertura
        );

        HistoricoStatusEntity primeiroHistorico = new HistoricoStatusEntity(
                ordemServico,
                StatusOrdemServico.RECEBIDA,
                RESPONSAVEL_SISTEMA,
                dataAbertura
        );

        ordemServico.adicionarHistoricoStatus(primeiroHistorico);
        ordemServicoRepository.save(ordemServico);

        return OrdemServicoMapper.paraResposta(ordemServico);
    }

    private String gerarNumeroProtocolo(LocalDateTime dataAbertura) {
        String data = dataAbertura.format(FORMATTER_DATA);
        String sufixo;
        String candidato;

        do {
            int valor = RandomGenerator.getDefault().nextInt(0x10000);
            sufixo = String.format("%04X", valor);
            candidato = "OS-" + data + "-" + sufixo;
        } while (ordemServicoRepository.existsByNumeroProtocolo(candidato));

        return candidato;
    }
}

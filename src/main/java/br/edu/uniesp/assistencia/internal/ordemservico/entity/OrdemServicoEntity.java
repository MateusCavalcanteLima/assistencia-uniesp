package br.edu.uniesp.assistencia.internal.ordemservico.entity;

import br.edu.uniesp.assistencia.internal.cliente.entity.ClienteEntity;
import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ordens_servico")
public class OrdemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_protocolo", nullable = false, length = 20, unique = true)
    private String numeroProtocolo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status;

    @Column(name = "descricao_problema", nullable = false, columnDefinition = "TEXT")
    private String descricaoProblema;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private EquipamentoEntity equipamento;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoStatusEntity> historicoStatus = new ArrayList<>();

    public OrdemServicoEntity(String numeroProtocolo,
                               String descricaoProblema,
                               ClienteEntity cliente,
                               EquipamentoEntity equipamento,
                               LocalDateTime dataAbertura) {
        this.numeroProtocolo = numeroProtocolo;
        this.descricaoProblema = descricaoProblema;
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.dataAbertura = dataAbertura;
        this.status = StatusOrdemServico.RECEBIDA;
    }

    public void adicionarHistoricoStatus(HistoricoStatusEntity historico) {
        historicoStatus.add(historico);
    }
}

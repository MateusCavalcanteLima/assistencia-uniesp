package br.edu.uniesp.assistencia.internal.ordemservico.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "historico_status")
public class HistoricoStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServicoEntity ordemServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status;

    @Column(nullable = false, length = 150)
    private String responsavel;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public HistoricoStatusEntity(OrdemServicoEntity ordemServico,
                                  StatusOrdemServico status,
                                  String responsavel,
                                  LocalDateTime dataHora) {
        this.ordemServico = ordemServico;
        this.status = status;
        this.responsavel = responsavel;
        this.dataHora = dataHora;
    }
}

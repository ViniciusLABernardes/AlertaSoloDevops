package br.com.AlertaSolo.services;


import br.com.AlertaSolo.dto.VerificarRiscoRequestDto;
import br.com.AlertaSolo.dto.VerificarRiscoResponseDto;
import br.com.AlertaSolo.entity.LocalRisco;
import br.com.AlertaSolo.entity.Sensor;
import br.com.AlertaSolo.entity.Usuario;
import br.com.AlertaSolo.exceptions.IdNaoEncontradoException;
import br.com.AlertaSolo.repository.LocalRiscoRepository;
import br.com.AlertaSolo.repository.SensorRepository;
import br.com.AlertaSolo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorService {

    @Autowired
    private LocalRiscoRepository localRiscoRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Sensor salvarSensor(Sensor sensor, long idLocalRisco) throws IdNaoEncontradoException {
        Sensor sensorNova = new Sensor();
        try {
            LocalRisco localRisco = localRiscoRepository.findById(idLocalRisco)
                    .orElseThrow(() -> new IdNaoEncontradoException("Local com id " + idLocalRisco + " não encontrado"));

            sensor.setLocalRisco(localRisco);
            sensor.setDataInstalacao(LocalDate.now());
            sensorNova = sensorRepository.save(sensor);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return sensorNova;
    }


    public Optional<Sensor> visualizarDadosSensorEspecifico(long id)  {
        return sensorRepository.findById(id);

    }

    @Transactional
    public VerificarRiscoResponseDto verificarRiscoDeslizamento(Long idLocal, double umidade, double inclinacao, double tremor) throws IdNaoEncontradoException {
        boolean umidadeAlta = umidade > 80;
        boolean inclinacaoPerigosa = inclinacao > 30;
        boolean tremorSignificativo = tremor > 5;

        VerificarRiscoResponseDto response = new VerificarRiscoResponseDto();



        if (umidadeAlta || inclinacaoPerigosa || tremorSignificativo) {
            response.setRiscoAlto(true);
            response.setMensagem("⚠️ RISCO ALTO DETECTADO!");



            LocalRisco local = localRiscoRepository.findById(idLocal).orElseThrow(()->new IdNaoEncontradoException("Local não encontrado!"));
            Sensor sensor = sensorRepository.findByLocalRisco(local).orElseThrow(() -> new IdNaoEncontradoException("Sensor não encontrado!"));

            sensor.setQntdAlerta(sensor.getQntdAlerta() + 1);
            sensorRepository.save(sensor);

            List<Usuario> usuarios = usuarioRepository.findByCidadeAndUf(local.getCidade(), local.getUf());

            List<String> notificacoes = usuarios.stream()
                    .map(u -> "🔔 Notificar " + u.getNome() + " em " + u.getCidade() + "/" + u.getUf())
                    .collect(Collectors.toList());

            response.setNotificacoes(notificacoes);
        } else {
            response.setRiscoAlto(false);
            response.setMensagem("✅ Situação estável.");
        }

        return response;
    }

}

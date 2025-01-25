package com.plataforma.gtv.service.impl;

import com.plataforma.gtv.domain.Endereco;
import com.plataforma.gtv.repository.EnderecoRepository;
import com.plataforma.gtv.service.EnderecoService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.plataforma.gtv.domain.Endereco}.
 */
@Service
@Transactional
public class EnderecoServiceImpl implements EnderecoService {

    private static final Logger LOG = LoggerFactory.getLogger(EnderecoServiceImpl.class);

    private final EnderecoRepository enderecoRepository;

    public EnderecoServiceImpl(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Override
    public Endereco save(Endereco endereco) {
        LOG.debug("Request to save Endereco : {}", endereco);
        return enderecoRepository.save(endereco);
    }

    @Override
    public Endereco update(Endereco endereco) {
        LOG.debug("Request to update Endereco : {}", endereco);
        return enderecoRepository.save(endereco);
    }

    @Override
    public Optional<Endereco> partialUpdate(Endereco endereco) {
        LOG.debug("Request to partially update Endereco : {}", endereco);

        return enderecoRepository
            .findById(endereco.getId())
            .map(existingEndereco -> {
                if (endereco.getRua() != null) {
                    existingEndereco.setRua(endereco.getRua());
                }
                if (endereco.getCep() != null) {
                    existingEndereco.setCep(endereco.getCep());
                }
                if (endereco.getCidade() != null) {
                    existingEndereco.setCidade(endereco.getCidade());
                }
                if (endereco.getEstado() != null) {
                    existingEndereco.setEstado(endereco.getEstado());
                }

                return existingEndereco;
            })
            .map(enderecoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Endereco> findAll() {
        LOG.debug("Request to get all Enderecos");
        return enderecoRepository.findAll();
    }

    /**
     *  Get all the enderecos where Aluno is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Endereco> findAllWhereAlunoIsNull() {
        LOG.debug("Request to get all enderecos where Aluno is null");
        return StreamSupport.stream(enderecoRepository.findAll().spliterator(), false)
            .filter(endereco -> endereco.getAluno() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Endereco> findOne(Long id) {
        LOG.debug("Request to get Endereco : {}", id);
        return enderecoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Endereco : {}", id);
        enderecoRepository.deleteById(id);
    }
}

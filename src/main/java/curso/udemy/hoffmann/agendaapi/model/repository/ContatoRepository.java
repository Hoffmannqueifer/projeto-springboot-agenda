package curso.udemy.hoffmann.agendaapi.model.repository;

import curso.udemy.hoffmann.agendaapi.model.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {

}

package curso.udemy.hoffmann.agendaapi.model.api.rest;

import curso.udemy.hoffmann.agendaapi.model.entity.Contato;
import curso.udemy.hoffmann.agendaapi.model.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contatos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContatoController {

    private final ContatoRepository contatoRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contato save( @RequestBody Contato contato){
        return contatoRepository.save(contato);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        contatoRepository.deleteById(id);
    }

    @GetMapping
    public Page<Contato> list(@RequestParam(value="page", defaultValue = "0") Integer pagina,
                              @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
        return contatoRepository.findAll(pageRequest);
    }

    @PatchMapping("{id}/favorito")
    public void favorite(@PathVariable Integer id){
        Optional<Contato> contato = contatoRepository.findById(id);
        contato.ifPresent(c ->{
            boolean favorito = c.getFavorito() ==Boolean.TRUE;
            c.setFavorito(!favorito);
            contatoRepository.save(c);
        });
    }

    @PutMapping("{id}/foto")
    public byte[] addPhoto(@PathVariable Integer id, @RequestParam("foto")Part arquivo){
        Optional<Contato> contato = contatoRepository.findById(id);
        return contato.map(c ->{
            try{
                InputStream is = arquivo.getInputStream();
                byte[] bytes = new byte[(int )arquivo.getSize()];
                IOUtils.readFully(is, bytes);
                c.setFoto(bytes);
                contatoRepository.save(c);
                is.close();
                return bytes;
            }catch (IOException e){
                return null;
            }
        }).orElse(null);
    }
}

package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@CrossOrigin(origins = "*") // libera acesso do END POINT de 1 ou todas origem
@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

	@Autowired /*Se fosse CDI seria @Inject*/
	private UsuarioRepository usuarioRepository;
	
	
	/*Serviço RESTful com mais de 1 parametro*/
	@GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id
			                                ,@PathVariable(value = "venda") Long venda ) {
		
        Optional<Usuario>  usuario  = usuarioRepository.findById(id);
		
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	/*Serviço RESTful*/
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {
		
        Optional<Usuario>  usuario  = usuarioRepository.findById(id);
		
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuarios(){
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
		
		//Relaciona o telefone ao usuario para cadastrar.
		for (int pos = 0; pos < usuario.getTelefones().size(); pos ++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo,HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
	public ResponseEntity<Usuario> cadastrarVenda(@PathVariable Long iduser,
			                                      @PathVariable Long idvenda){
		
		//Aqui seria o processo de venda
		//Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity("id user:" + iduser + " idvenda :" +idvenda, HttpStatus.OK);
		
	}
	
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){
		
		
		//Relaciona o telefone ao usuario para atualizar.
		for (int pos = 0; pos < usuario.getTelefones().size(); pos ++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo,HttpStatus.OK);
		
	}
	
	@PutMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
	public ResponseEntity<Usuario> updateVenda(@PathVariable Long iduser,
			                                   @PathVariable Long idvenda){
		
		//Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity("Venda atualizada : " + "id user:" + iduser + " idvenda :" +idvenda, HttpStatus.OK);
		
	}
	@PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Usuario usuario) {

        return usuarioRepository.findById(id).map(record -> {
            record.setLogin(usuario.getLogin());
            record.setNome(usuario.getNome());
            record.setSenha(usuario.getSenha());
            Usuario atualizado = usuarioRepository.save(record);
            return ResponseEntity.ok().body(atualizado);
        }).orElse(ResponseEntity.notFound().build());
	
	}
	
	@DeleteMapping(value = "/{id}" , produces = "application/text")
	public String delete (@PathVariable("id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return "ok"; 
				
	}
	
	@DeleteMapping(value = "/{id}/venda" , produces = "application/text")
	public String deletevenda(@PathVariable("id") Long id) {
		
		usuarioRepository.deleteById(id); //iria deletar todas as vendas do usuário
		
		return "ok"; 
	}
	
	
}
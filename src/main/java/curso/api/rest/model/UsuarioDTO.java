package curso.api.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
    
	private String userLogin;
	private String userNome;
	
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Telefone> telefones = new ArrayList<Telefone>();

	
	public UsuarioDTO(Usuario usuario) {
		this.userLogin = usuario.getLogin();
		this.userNome  = usuario.getNome();
		this.telefones = usuario.getTelefones();
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}	
	public String getUserLogin() {
		return userLogin;
	}	
	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}	
	public String getUserNome() {
		return userNome;
	}
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	
}

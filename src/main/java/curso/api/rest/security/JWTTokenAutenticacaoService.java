package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	
	//estabelecendo algumas constantes com valores que não pode alterar
	
	/*tempo de validade do token em milisegundos - 2 dias*/
	private static final long EXPIRATION_TIME = 172800000;
	
	/*senha unica para compor a autenticacao e ajudar na segurança*/
	private static final String SECRET = "SenhaExtremamenteSecreta";
	
	/*Prefixo padrao de Token*/
	private static final String TOKEN_PREFIX = "Bearer";
	
	/*Prefixo que sera retornado na reposta do cabecalho. Sera utilizado para identificar o Token*/
	private static final String HEADER_STRING = "Authorization";

	/*Gerando token de atuenticacao e adicionando ao cabecalho e resposta HTTP*/
	public void addAuthentication(HttpServletResponse response,String username) throws IOException {
		
		/*Montagem do Token*/
		String JWT = Jwts.builder() /*chama o gerador de Token*/
				        .setSubject(username)/*Adiciona o usuario*/
				        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME )) /*tempo de expiracao*/
				        .signWith(SignatureAlgorithm.HS512, SECRET).compact(); /*compactacao e algoritimos de geracao de senha*/

		/*Junta o token com o prefixo*/
		String token = TOKEN_PREFIX + " " + JWT; /*Bearer 3213219adsdsa921321321sdsa*/
		
		/*Adiciona no cabecalho HTTP*/
		response.addHeader(HEADER_STRING, token);/*Authorization: Bearer 3213219adsdsa921321321sdsa */
		
		//Liberando a resposta para portas diferentes que usam a API ou caso clientes Web
		liberacaoCors(response);
		
		/*Escreve token como resposta no corpo HTTP*/
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
    /*Retorna o usuario validado com token ou caso nao seja valido retorna null*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		/*Pega o Token enviado no cabecalho HTTP*/		
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			
			String tokenSemBearer = token.replace(TOKEN_PREFIX, "").trim();
			
			/*Faz a validacao do Token do usuario na requisicao*/
			String user = Jwts.parser().setSigningKey(SECRET) // Bearer 3213219adsdsa921321321sdsa
					          .parseClaimsJws(tokenSemBearer) //3213219adsdsa921321321sdsa
					          .getBody().getSubject();//Elton
			
			if(user != null) {
				
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				if (usuario != null) {
					
					if (tokenSemBearer.equalsIgnoreCase(usuario.getToken())) {
     					System.out.println("TOKEN..:"+ token);
			  		    return new UsernamePasswordAuthenticationToken(
							   usuario.getLogin(), usuario.getSenha(),usuario.getAuthorities());	
					}
				}
			}
		}
		
		liberacaoCors(response);
		
		return null ; /*Nao Autorizado*/
		
	}

	private void liberacaoCors(HttpServletResponse response) {

		if(response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
	}
	
	
}

package curso.api.rest.security;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

/*mapeia,URL, endereços, autoriza ou bloqueio acessos a URL*/

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	/*configura as solicitacoes de acessos por HTTP*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
    
		/*ativando a protecao contra usuario que não estao validados por token*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*Ativando a permissão para acesso a página inicial do sistema. Ex. sistema.com.br/index*/
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		/*URL Logout - redireciona apos o usuario desologar do sistema*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*Mapeia a URL e Logout e invalida o usuario*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/*Filtra as requisicoes de login para autenticacao*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
				UsernamePasswordAuthenticationFilter.class)
		
		/*Filtra demais requisicoes para verificar a presenca do TOKEN JWT no HEADER HTTP*/
		.addFilterBefore(new JWTApiAutenticacaoFilter(),UsernamePasswordAuthenticationFilter.class);
		
		
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
   	
	   /* Service que irá consultar o usuário no banco de dados */	
	   auth.userDetailsService(implementacaoUserDetailsService)
	   /*padrao de codificacao de senha do usuario*/
	   .passwordEncoder(new BCryptPasswordEncoder());
	
	}
	

}

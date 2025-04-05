//package com.rosist.kardex.security1.model;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//public class UsuarioDetails implements UserDetails {
//
//	private final Usuario usuario;
//    private Collection<? extends GrantedAuthority> authorities;
//	
//	public UsuarioDetails(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
//		this.usuario = usuario;
//        this.authorities = authorities;
//	}
//
//    public static UsuarioDetails build(Usuario usuario){
//        List<GrantedAuthority> authorities =
//                usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
//                .getNombre())).collect(Collectors.toList());
//        return new UsuarioDetails(usuario, authorities);
////        return new UsuarioDetails(usuario.getNombre(), usuario.getUsername(), usuario.getEmail(), usuario.getPassword(), authorities);
//    }
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return usuario.getRoles().stream()
//				.map(authority -> new SimpleGrantedAuthority(authority.toString())).collect(Collectors.toList());
////		return List.of(()->usuario.getRoles());
//	}
//
//	@Override
//	public String getPassword() {
//		return usuario.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return usuario.getUsername();
//	}
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//	
//}

package dev.arielalvesdutra.hcrpr.it.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import dev.arielalvesdutra.hcrpr.builders.TechniqueBuilder;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateTechniqueDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveTechniqueDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateTechniqueDTO;
import dev.arielalvesdutra.hcrpr.dto.builders.CreateTechniqueDTOBuilder;
import dev.arielalvesdutra.hcrpr.dto.builders.UpdateTechniqueDTOBuilder;
import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.repositories.TechniqueRepository;
import dev.arielalvesdutra.hcrpr.services.TechniqueService;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TechniqueControllerIT {
	
	@Autowired
	private TechniqueRepository techniqueRepository;
	
	@Autowired
	private TechniqueService techniqueService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@After
	public void tearDown() {
		techniqueRepository.deleteAll();
	}
	
	@Test
	public void create_shouldWorkAndReturn201() {
		CreateTechniqueDTO createTechniqueDto = new CreateTechniqueDTOBuilder()
				.withName("Técnica X")				
				.withDescription("A técnica X")
				.build();
		
		
		ResponseEntity<RetrieveTechniqueDTO> response = 
				restTemplate.postForEntity("/techniques", createTechniqueDto, RetrieveTechniqueDTO.class);
		RetrieveTechniqueDTO retrievedTechniqueDto = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().containsKey("Location")).isTrue();
		assertThat(retrievedTechniqueDto.getName()).isEqualTo(createTechniqueDto.getName());
		assertThat(retrievedTechniqueDto.getDescription()).isEqualTo(createTechniqueDto.getDescription());
		assertThat(retrievedTechniqueDto.getCreatedAt()).isNotNull();
		assertThat(retrievedTechniqueDto.getId()).isNotNull();
	}
	
	@Test
	public void retrieveAll_shouldWork() {
		
		Technique createdTechnique = this.buildAndSaveASimpleTechnique();
		RetrieveTechniqueDTO expectedTechnique = new RetrieveTechniqueDTO(createdTechnique);
	
		ResponseEntity<PagedModel<RetrieveTechniqueDTO>> response = restTemplate.exchange(
				"/techniques",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<PagedModel<RetrieveTechniqueDTO>>() {});
		PagedModel<RetrieveTechniqueDTO> resources = response.getBody();
		List<RetrieveTechniqueDTO> techniques = new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(techniques).size().isEqualTo(1);
		assertThat(techniques).contains(expectedTechnique);
	}
	
	@Test
	public void retrieveById_shouldWork() {
		Technique createdTechnique = this.buildAndSaveASimpleTechnique();
		RetrieveTechniqueDTO expectedTechnique = new RetrieveTechniqueDTO(createdTechnique);
		String url = "/techniques/" + expectedTechnique.getId();
		
		ResponseEntity<RetrieveTechniqueDTO> response = 
				restTemplate.getForEntity(url, RetrieveTechniqueDTO.class);
		RetrieveTechniqueDTO retrievedTechniqueDto = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedTechniqueDto).isEqualTo(expectedTechnique);
	}
	
	@Test
	public void retrieveById_withoutTechnique_shouldReturn404() {
		String url = "/techniques/" + 1555L;
		
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void deleteById_shouldWork() {
		Technique createdTechnique = this.buildAndSaveASimpleTechnique();
		String url = "/techniques/" + createdTechnique.getId();
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.DELETE,
				null,
				String.class);
		Optional<Technique> fetchedTechnique = 
				this.techniqueRepository.findById(createdTechnique.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedTechnique.isPresent()).isFalse();
	}
	
	@Test
	public void updateById_shouldWork() {
		Technique createdTechnique = this.buildAndSaveASimpleTechnique();
		String url = "/techniques/" + createdTechnique.getId();		
		HttpHeaders headers = new HttpHeaders();
		UpdateTechniqueDTO updateTechniqueDto = new UpdateTechniqueDTOBuilder()
				.withName("Técnica atualizada")
				.withDescription("Descrição atualizada")
				.build();
		HttpEntity<UpdateTechniqueDTO> httpEntity = 
				new HttpEntity<UpdateTechniqueDTO>(updateTechniqueDto, headers);
		
		
		ResponseEntity<RetrieveTechniqueDTO> response = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				httpEntity,
				RetrieveTechniqueDTO.class);
		RetrieveTechniqueDTO responseTechnique = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(responseTechnique.getName()).isEqualTo(updateTechniqueDto.getName());
		assertThat(responseTechnique.getDescription()).isEqualTo(updateTechniqueDto.getDescription());
		assertThat(responseTechnique.getId()).isEqualTo(createdTechnique.getId());
		assertThat(responseTechnique.getCreatedAt().isEqual(createdTechnique.getCreatedAt())).isTrue();
	}

	private Technique buildAndSaveASimpleTechnique() {
		Technique technique = new TechniqueBuilder()
				.withName("Técnica X")
				.withDescription("A técnica X")
				.build();
		
		return this.techniqueService.create(technique);
	}
}

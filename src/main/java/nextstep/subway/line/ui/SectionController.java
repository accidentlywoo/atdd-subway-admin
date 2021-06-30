package nextstep.subway.line.ui;

import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.SectionCreateReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/line/sections")
public class SectionController {
	private final SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = new SectionService();
	}

	@PostMapping
	public ResponseEntity<Section> createSection(@RequestBody SectionCreateReq req) {
		Section section = sectionService.createSection(req);
		return ResponseEntity.created(URI.create("/line/sections" + section.getId())).body(section);
	}

	@GetMapping
	public void allLineSection() {
	}

	@GetMapping("/{id}")
	public void findSecion(@PathVariable String id) {
	}

	@PatchMapping("/{id}")
	public void updateLine(@PathVariable String id) {
	}

	@DeleteMapping("/{id}")
	public void deleteLine(@PathVariable String id) {
	}
}

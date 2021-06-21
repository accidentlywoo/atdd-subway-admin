package nextstep.subway.line.ui;

import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.dto.LineRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/line/sections")
public class SectionController {
	private final SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = new SectionService();
	}

	@PostMapping
	public void createSection() {
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

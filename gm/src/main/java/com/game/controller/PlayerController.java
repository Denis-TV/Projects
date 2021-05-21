package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class PlayerController {
    private final PlayerService playerService;
    //private List<Player> players;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/players")
    public ResponseEntity<List<Player>> read(@RequestParam Map<String, String> params) {
        int pageNumber = 0;
        if (params.containsKey("pageNumber")) {
            try {
                pageNumber = Integer.parseInt(params.get("pageNumber"));
                if (pageNumber < 0) pageNumber = 0;
            } catch (NumberFormatException e) {}
        }
        int pageSize = 3;
        if (params.containsKey("pageSize")) {
            try {
                pageSize = Integer.parseInt(params.get("pageSize"));
                if (pageSize <= 0) pageSize = 3;
            } catch (NumberFormatException e) {}
        }
        PlayerOrder playerOrder = PlayerOrder.ID;
        if (params.containsKey("order")) {
            try {
                playerOrder = PlayerOrder.valueOf(params.get("order"));
            } catch (IllegalArgumentException e) {}
        }
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, playerOrder.getFieldName()));
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(orders));

        final List<Player> players = playerService.readAll(getSpecification(params), page);

        return new ResponseEntity<>(players, HttpStatus.OK);
        /*return players != null &&  !players.isEmpty()
                ? new ResponseEntity<>(players, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);*/
    }

    @GetMapping(value = "/players/{id}")
    public ResponseEntity<Player> read(@PathVariable(name = "id") Long id) {
        if (!isValidId(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = playerService.getPlayer(id);
        return player != null
                ? new ResponseEntity<>(player, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/players/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCount(@RequestParam Map<String, String> params) {
        return   playerService.readAll(getSpecification(params)).size();
    }

    @PostMapping("/players")
    public ResponseEntity<Player> newPlayer(@RequestBody Player requestPlayer) {
        Player player = new Player();
        if (player.fillParams(false, requestPlayer)) {
            playerService.create(player);
            player = playerService.create(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> newPlayer(@PathVariable(name = "id") Long id, @RequestBody Player requestPlayer) {
        if (!isValidId(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = playerService.getPlayer(id);
        if (player == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (player.fillParams(true, requestPlayer)
                && playerService.update(player, id)) {
            return new ResponseEntity<>(player, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        if (!isValidId(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return playerService.delete(id)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private boolean isValidId(Long id) {
        if (id == null || id<1) return false;
        return true;
    }

    private Specification<Player> updateSpecification(Specification<Player> targetSpecification, Specification<Player> sourceSpecification) {
        if (sourceSpecification == null) return targetSpecification;
        if (targetSpecification == null) return Specification.where(sourceSpecification);
        return targetSpecification.and(sourceSpecification);
    }

    private Specification<Player> getSpecification(Map<String, String> params) {
        Specification<Player> specification = null;
        specification = updateSpecification(specification, playerService.filterName(params.get("name")));
        specification = updateSpecification(specification, playerService.filterTitle(params.get("title")));
        specification = updateSpecification(specification, playerService.filterBirthdayFrom(params.get("after")));
        specification = updateSpecification(specification, playerService.filterBirthdayTo(params.get("before")));
        specification = updateSpecification(specification, playerService.filterExperienceFrom(params.get("minExperience")));
        specification = updateSpecification(specification, playerService.filterExperienceTo(params.get("maxExperience")));
        specification = updateSpecification(specification, playerService.filterLevelFrom(params.get("minLevel")));
        specification = updateSpecification(specification, playerService.filterLevelTo(params.get("maxLevel")));
        specification = updateSpecification(specification, playerService.filterRace(params.get("race")));
        specification = updateSpecification(specification, playerService.filterProfession(params.get("profession")));
        specification = updateSpecification(specification, playerService.filterBanned(params.get("banned")));
        //specification = updateSpecification(specification, playerService.filterBirthday(params.get("after"),params.get("before")));
        return specification;
    }

}

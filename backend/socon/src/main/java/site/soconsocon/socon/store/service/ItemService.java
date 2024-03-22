package site.soconsocon.socon.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.soconsocon.socon.global.domain.ErrorCode;
import site.soconsocon.socon.global.exception.SoconException;
import site.soconsocon.socon.store.domain.dto.request.AddItemRequest;
import site.soconsocon.socon.store.domain.dto.request.MemberRequest;
import site.soconsocon.socon.store.domain.dto.response.ItemListResponse;
import site.soconsocon.socon.store.domain.entity.jpa.Item;
import site.soconsocon.socon.store.domain.entity.jpa.Store;
import site.soconsocon.socon.store.exception.StoreErrorCode;
import site.soconsocon.socon.store.exception.StoreException;
import site.soconsocon.socon.store.repository.ItemRepository;
import site.soconsocon.socon.store.repository.StoreRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;
    // 상품 정보 등록
    public void saveItem(AddItemRequest request, Integer storeId, MemberRequest memberRequest) {

        Store savedStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND, "" + storeId));

        if (Objects.equals(savedStore.getMemberId(), memberRequest.getMemberId())){
            // 점포 소유주 불일치
            throw new SoconException(ErrorCode.FORBIDDEN,  "점포 소유주 아님" + memberRequest.getMemberId());
        }
        itemRepository.save(Item.builder()
                .name(request.getName())
                .image(request.getImage())
                .price(request.getPrice())
                .summary(request.getSummary())
                .description(request.getDescription())
                .store(savedStore)
                .build());
    }
    // 점주 가게 상품 목록 조회
    public List<ItemListResponse> getItemList(Integer StoreId, MemberRequest memberRequest) {

        Integer storeMemberId = storeRepository.findMemberIdByStoreId(StoreId);

        if(!Objects.equals(storeMemberId, memberRequest.getMemberId())){
            // 점포 소유주 불일치
            throw new SoconException(ErrorCode.FORBIDDEN,  "점포 소유자 아님" + memberRequest.getMemberId());
        }
        return itemRepository.findItemsByStoreId(StoreId);
    }

    // 상품 정보 상세 조회
    public Item getDetailItemInfo(Integer storeId, Integer itemId, MemberRequest memberRequest) {

        if(!Objects.equals(memberRequest.getMemberId(), storeRepository.findMemberIdByStoreId(storeId))) {
            throw new SoconException(ErrorCode.FORBIDDEN,  "점포 소유자 아님" + memberRequest.getMemberId());
        }
            return itemRepository.findById(itemId)
                    .orElseThrow(() -> new StoreException(StoreErrorCode.ITEM_NOT_FOUND, "" + itemId));
    }
}

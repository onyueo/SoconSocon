package site.soconsocon.socon.store.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.soconsocon.socon.global.domain.ErrorCode;
import site.soconsocon.socon.global.exception.SoconException;
import site.soconsocon.socon.store.domain.dto.request.AddIssueRequest;
import site.soconsocon.socon.store.domain.dto.request.AddMySoconRequest;
import site.soconsocon.socon.store.domain.dto.response.IssueInfoResponse;
import site.soconsocon.socon.store.domain.dto.response.IssueListResponse;
import site.soconsocon.socon.store.domain.entity.jpa.Issue;
import site.soconsocon.socon.store.domain.entity.jpa.Item;
import site.soconsocon.socon.store.domain.entity.jpa.Socon;
import site.soconsocon.socon.store.exception.StoreErrorCode;
import site.soconsocon.socon.store.exception.StoreException;
import site.soconsocon.socon.store.repository.IssueRepository;
import site.soconsocon.socon.store.repository.ItemRepository;
import site.soconsocon.socon.store.repository.SoconRepository;
import site.soconsocon.socon.store.repository.StoreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final ItemRepository itemRepository;
    private final SoconRepository soconRepository;
    private final StoreRepository storeRepository;


    // 발행 목록 조회
    public List<IssueListResponse> getIssueList(Integer storeId) {

        List<Issue> issues = issueRepository.findIssueListByStoreId(storeId);
        List<IssueListResponse> issueList = new ArrayList<>();
        for (Issue issue : issues) {
            issueList.add(IssueListResponse.builder()
                    .id(issue.getId())
                    .isMain(issue.getIsMain())
                    .name(issue.getName())
                    .image(issue.getImage())
                    .issuedQuantity(issue.getIssuedQuantity())
                    .leftQuantity(issue.getMaxQuantity() - issue.getIssuedQuantity())
                    .isDiscounted(issue.getIsDiscounted())
                    .price(issue.getPrice())
                    .discountedPrice(issue.getDiscountedPrice())
                    .createdAt(issue.getCreatedAt())
                    .build());
        }
        return issueList;
    }

    // 발행 정보 등록
    public void saveIssue(AddIssueRequest request,
                          Integer storeId,
                          Integer itemId,
                          int memberId) {
        if (!Objects.equals(memberId, storeRepository.findMemberIdByStoreId(storeId))) {
            throw new SoconException(ErrorCode.FORBIDDEN);
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.ITEM_NOT_FOUND));

        issueRepository.save(Issue.builder()
                .storeName(storeRepository.findNameByStoreId(storeId))
                .name(item.getName())
                .image(item.getImage())
                .isMain(request.getIsMain())
                .price(item.getPrice())
                .isDiscounted(request.getIsDiscounted())
                .discountedPrice(request.getDiscountedPrice())
                .maxQuantity(request.getMaxQuantity())
                .issuedQuantity(0)
                .used(0)
                .period(request.getPeriod())
                .createdAt(LocalDate.now())
                .item(item)
                .status('A')
                .build());
    }

    // 소콘북 저장
    public void saveMySocon(AddMySoconRequest request) {

        Issue issue = issueRepository.findById(request.getIssueId())
                .orElseThrow(() -> new StoreException(StoreErrorCode.ISSUE_NOT_FOUND));
        if (issue.getStatus() != 'A') {
            // 발행 중 아님
            throw new StoreException(StoreErrorCode.INVALID_ISSUE);
        }
        if (issue.getMaxQuantity() - issue.getIssuedQuantity() < request.getPurchasedQuantity()) {
            // 발행 가능 개수보다 요청한 개수가 많을 경우
            throw new StoreException(StoreErrorCode.ISSUE_MAX_QUANTITY);
        }
        issue.setIssuedQuantity(issue.getIssuedQuantity() + request.getPurchasedQuantity());
        issueRepository.save(issue);

        for (int i = 0; i < request.getPurchasedQuantity(); i++) {
            soconRepository.save(Socon.builder()
                    .purchasedAt(request.getPurchaseAt())
                    .expiredAt(request.getExpiredAt())
                    .usedAt(request.getUsedAt())
                    .status(request.getStatus())
                    .issue(issue)
                    .memberId(request.getMemberId())
                    .build());
        }
    }

    // 발행 중지
    public void stopIssue(Integer issueId, int memberId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.ISSUE_NOT_FOUND));

        if (!Objects.equals(issueRepository.findMemberIdByIssueId(issueId), memberId)) {
            // 본인 점포의 상품이 아닐 경우
            throw new SoconException(ErrorCode.FORBIDDEN);
        }
        if (issue.getStatus() != 'A') {
            // 발행 중 아님
            throw new SoconException(ErrorCode.BAD_REQUEST);
        }
        issue.setStatus('I');
        issueRepository.save(issue);
    }

    public Object getIssueInfo(Integer issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.ISSUE_NOT_FOUND));
        Item item = itemRepository.findById(issue.getItem().getId())
                .orElseThrow(() -> new StoreException(StoreErrorCode.ITEM_NOT_FOUND));
        return IssueInfoResponse.builder()
                .id(issue.getId())
                .name(issue.getName())
                .itemImage(issue.getImage())
                .storeImage(item.getStore().getImage())
                .price(issue.getPrice())
                .summary(item.getSummary())
                .description(item.getDescription())
                .build();
    }
}

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:socon/utils/colors.dart';
import 'package:socon/utils/fontSizes.dart';
import 'package:socon/views/atoms/image_card.dart';
import 'package:socon/views/modules/app_bar.dart';
import 'package:socon/views/modules/search_module.dart';

import '../../models/store.dart';
import '../../utils/responsive_utils.dart';
import '../../viewmodels/stores_view_model.dart';
import '../atoms/search_box.dart';
import '../modules/place_list.dart';

// 주변 장소 조회 페이지
class NearbyInfoScreen extends StatefulWidget {
  final String userName = "도휘리릭";
  final int number = 100;

  @override
  State<StatefulWidget> createState() {
    return _NearbyInfoScreenState();
  }
}

class _NearbyInfoScreenState extends State<NearbyInfoScreen> {
  List<Store> stores = [
    Store(
        storeId: 20,
        name: "슬슬베이크샵",
        imageUrl:
            "https://firebasestorage.googleapis.com/v0/b/socon-socon.appspot.com/o/images%2Fsocon%2FKakaoTalk_20240404_065435031_01-removebg-preview.png?alt=media&token=f0c45c35-adbf-47f0-8cb8-3df6a2294588",
        address: "광주 광산구 장신로 44",
        category: "카페",
        createdAt: "2024-04-04",
        isLike: true,
        mainSocon: "생딸기 빙수",
        distance: 15),
    Store(
        storeId: 20,
        name: "오소유",
        imageUrl:
            "https://firebasestorage.googleapis.com/v0/b/socon-socon.appspot.com/o/images%2Fsocon%2Fsocon2.png?alt=media&token=e7736390-6c7b-4d96-97fa-02e9383ab60c",
        address: "광주 광산구 장덕로40번길 13-1 1층",
        category: "음식점",
        createdAt: "2024-03-22",
        isLike: true,
        mainSocon: "소금빵",
        distance: 15),
    Store(
        storeId: 20,
        name: "잇포",
        imageUrl:
            "https://firebasestorage.googleapis.com/v0/b/socon-socon.appspot.com/o/images%2Fsocon%2Fitpo-removebg-preview.png?alt=media&token=ca3f0b40-25ba-48d4-b3ba-a7edf1547dd2",
        address: "광주 광산구 장신로19번안길 23-2 1층",
        category: "음식점",
        createdAt: "2024-03-24",
        isLike: false,
        mainSocon: "갈비살 쌀국수",
        distance: 20),
    Store(
        storeId: 20,
        name: "가마솥토종순대국",
        imageUrl:
            "https://firebasestorage.googleapis.com/v0/b/socon-socon.appspot.com/o/images%2Fsocon%2Fsundae-removebg-preview.png?alt=media&token=51584b19-ff28-4af2-8375-c2adf5effbaf",
        address: "광주 광산구 장신로20번안길 40-1",
        category: "음식점",
        createdAt: "2024-03-24",
        isLike: false,
        mainSocon: "순대국",
        distance: 15),
    Store(
        storeId: 20,
        name: "르부케",
        imageUrl:
            "https://firebasestorage.googleapis.com/v0/b/socon-socon.appspot.com/o/images%2Fsocon%2FKakaoTalk_20240404_065435031-removebg-preview.png?alt=media&token=cba19766-006a-45cd-8794-97d8e65dea91",
        address: "광주 광산구 장덕로50번길 10 1층",
        category: "카페",
        createdAt: "2024-03-25",
        isLike: true,
        mainSocon: "생딸기 빙수",
        distance: 15)
  ];

  // @override
  // void initState() {
  //   super.initState();
  //   Future.microtask(() {
  //     Provider.of<StoresViewModel>(context, listen: false).searchStores();
  //   });
  // }
  StoresViewModel _storesViewModel = StoresViewModel();

  // Future<List<Store>?> _fetchStores() async {
  //   try {
  // await Future.delayed(Duration(seconds: 2));

  // List<Store>? storesData = await _storesViewModel.searchStores();

  // print("_fetchStores result: $storesData");
  // return storesData;
  //     return stores;
  //   } catch (error) {
  //     print("Error _fetchStores: $error");
  //     return null;
  //   }
  // }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.WHITE,
      appBar: CustomAppBar(title: "소콘소콘"),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            // 상태가 변경될 때만 해당 부분을 다시 빌드하도록 Consumer를 사용
            ImageCard(
              imgUrl:
                  "https://firebasestorage.googleapis.com/v0/b/socon-socon.appspot.com/o/images%2Fbanner%2Fbanner_maratang.png?alt=media&token=c3ac6662-a3da-49f1-b02b-b7c3db771180",
              width: ResponsiveUtils.getWidthWithPixels(context, 320),
              height: ResponsiveUtils.getHeightWithPixels(context, 88),
            ),
            const SizedBox(height: 15.0),
            SearchModule(
              type: "nearby",
              onUpdateStores: (stores) {
                setState(() {
                  this.stores = stores;
                });
              },
            ),
            SizedBox(height: 10.0),
            Expanded(
              child: SizedBox(
                width: ResponsiveUtils.getWidthWithPixels(context, 320),
                child: PlaceList(stores: stores),
              ),
            ),
          ],
        ),
      ),
      // body: FutureBuilder<List<Store>?>(
      //   future: _fetchStores(),
      //   builder: (context, snapshot) {
      //     if (snapshot.connectionState == ConnectionState.waiting) {
      //       return Center(child: CircularProgressIndicator());
      //     } else if (snapshot.hasError) {
      //       return Center(child: Text('Error: ${snapshot.error}'));
      //     } else {
      //       List<Store>? storesData = snapshot.data;
      //       print("storesData를 찍어보자 $storesData");
      //
      //       if (storesData != null) {
      //         return Center(
      //           child: Column(
      //             mainAxisAlignment: MainAxisAlignment.center,
      //             crossAxisAlignment: CrossAxisAlignment.center,
      //             children: [
      //               // 상태가 변경될 때만 해당 부분을 다시 빌드하도록 Consumer를 사용
      //               ImageCard(
      //                 imgUrl:
      //                 "https://firebasestorage.googleapis.com/v0/b/socon-socon.appspot.com/o/images%2Fbanner%2Fbanner_maratang.png?alt=media&token=c3ac6662-a3da-49f1-b02b-b7c3db771180",
      //                 width: ResponsiveUtils.getWidthWithPixels(context, 320),
      //                 height: ResponsiveUtils.getHeightWithPixels(context, 88),
      //               ),
      //               const SizedBox(height: 15.0),
      //               SearchModule(type: "nearby"),
      //               SizedBox(height: 10.0),
      //               Expanded(
      //                 child: SizedBox(
      //                   width: ResponsiveUtils.getWidthWithPixels(context, 320),
      //                   child: PlaceList(stores: storesData),
      //                 ),
      //               ),
      //             ],
      //           ),
      //         );
      //       }
      //     }
      //     return SizedBox.shrink(); // 반환하지 않을 경우 에러 발생 가능
      //   },
      // ),
    );
  }

  Widget availableSoconInfo() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Text(
          "${widget.userName}님, 현재 사용 가능한 소콘이",
          style: TextStyle(
            fontSize: ResponsiveUtils.calculateResponsiveFontSize(
                context, FontSizes.XXXSMALL),
            color: AppColors.BLACK,
          ),
        ),
        Text(
          "${widget.number}",
          style: TextStyle(
            fontSize: ResponsiveUtils.calculateResponsiveFontSize(
                context, FontSizes.SMALL),
          ),
        ),
        Text(
          "개 남아있어요.",
          style: TextStyle(
            fontSize: ResponsiveUtils.calculateResponsiveFontSize(
                context, FontSizes.XXXSMALL),
            color: AppColors.BLACK,
          ),
        )
      ],
    );
  }
}

// Store tempStore = Store(
//     storeId: 20,
//     name: "오소유",
//     imageUrl: "https://cataas.com/cat",
//     address: "광주 광산구 장덕로40번길 13-1 1층",
//     category: "음식점",
//     createdAt: "2024-03-22",
//     isLike: true,
//     mainSocon: "소금빵",
//     distance: 15);

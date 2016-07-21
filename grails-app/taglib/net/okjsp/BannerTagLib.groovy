package net.okjsp

import grails.transaction.Transactional

class BannerTagLib {
    
    def randomService

    /**
     * @attr type REQUIRED
     */
    def banner = { attrs, body ->
        
        def bannerType = attrs.type
        
        if(!(bannerType instanceof BannerType)) bannerType = BannerType.valueOf(bannerType)

        def banner

        Banner.withNewTransaction {

            def banners = Banner.where {
                type == bannerType && visible == true
            }.list()

            banner = banners ? randomService.draw(banners) : null
        }

        if(banner) {
            def bannerHTML = ""

            def target = """target=\"${banner.target}\""""

            switch (bannerType) {

                case BannerType.MAIN_RIGHT :
                case BannerType.SUB_RIGHT :
                    bannerHTML = """<div class="right-banner">
                                    <a href="${request.contextPath}/banner/stats/${banner.id}" ${banner.target ? target : ''}><img src="${banner.image}" style="width:160px;"/></a>
                                </div>"""
                    break
                case BannerType.MAIN :
                    bannerHTML = """<div class="main-banner-wrapper">
                                    <div class="main-banner"><a href="${request.contextPath}/banner/stats/${banner.id}" ${banner.target ? target : ''}><img src="${banner.image}" /></a></div>
                                </div>"""
                    break
                case BannerType.CONTENT :
                    bannerHTML = """<div class="sub-banner-wrapper">
                                    <div class="sub-banner"><a href="${request.contextPath}/banner/stats/${banner.id}" ${banner.target ? target : ''}><img src="${banner.image}" /></a></div>
                                </div>"""
                    break
            }

            out << bannerHTML
        }
    }
}

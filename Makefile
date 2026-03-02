# Makefile for meta-agentos

KAS ?= kas
KAS_YML ?= scripts/kas/kas-rpi5.yml
BUILD_DIR ?= build

.PHONY: all build clean shell help flash list-images

## Default target
all: build

## build: Run kas build
build:
	$(KAS) build $(KAS_YML)

## shell: Open kas shell
shell:
	$(KAS) shell $(KAS_YML)

## clean: Clean build artifacts (yocto tmp and downloads)
clean:
	rm -rf $(BUILD_DIR)/tmp $(BUILD_DIR)/sstate-cache downloads

## list-images: List generated SD card images
list-images:
	@echo "Generated SD images:"
	@find $(BUILD_DIR)/tmp/deploy/images -type f -name '*.wic.bz2' || true

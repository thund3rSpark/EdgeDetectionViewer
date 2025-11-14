class EdgeDetectionViewer {
    private canvas: HTMLCanvasElement;
    private ctx: CanvasRenderingContext2D;
    private fpsElement: HTMLElement;
    private resolutionElement: HTMLElement;
    private frameCount: number = 0;
    private lastFpsUpdate: number = 0;

    constructor() {
        this.canvas = document.getElementById('outputCanvas') as HTMLCanvasElement;
        this.ctx = this.canvas.getContext('2d')!;
        this.fpsElement = document.getElementById('fps')!;
        this.resolutionElement = document.getElementById('resolution')!;

        this.initializeEventListeners();
        this.loadSampleImage();
        this.startFpsCounter();
    }

    private initializeEventListeners(): void {
        const fileInput = document.getElementById('imageInput') as HTMLInputElement;
        fileInput.addEventListener('change', (event) => {
            this.handleImageUpload(event);
        });

        const processButton = document.getElementById('processButton') as HTMLButtonElement;
        processButton.addEventListener('click', () => {
            this.applyEdgeDetection();
        });
    }

    private loadSampleImage(): void {
        const sampleImage = new Image();
        sampleImage.onload = () => {
            this.drawImage(sampleImage);
            this.updateResolution(sampleImage.width, sampleImage.height);
        };
        sampleImage.src = this.createSampleImage();
    }

    private createSampleImage(): string {
        const canvas = document.createElement('canvas');
        canvas.width = 640;
        canvas.height = 480;
        const ctx = canvas.getContext('2d')!;

        // Draw sample image with clear edges
        ctx.fillStyle = '#ffffff';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        ctx.fillStyle = '#ff0000';
        ctx.fillRect(50, 50, 100, 100);

        ctx.fillStyle = '#00ff00';
        ctx.beginPath();
        ctx.arc(400, 150, 75, 0, 2 * Math.PI);
        ctx.fill();

        ctx.strokeStyle = '#0000ff';
        ctx.lineWidth = 3;
        ctx.strokeRect(200, 300, 150, 100);

        return canvas.toDataURL();
    }

    private drawImage(image: HTMLImageElement): void {
        this.canvas.width = image.width;
        this.canvas.height = image.height;
        this.ctx.drawImage(image, 0, 0);
        this.frameCount++;
    }

    private handleImageUpload(event: Event): void {
        const input = event.target as HTMLInputElement;
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = (e) => {
                const img = new Image();
                img.onload = () => {
                    this.drawImage(img);
                    this.updateResolution(img.width, img.height);
                };
                img.src = e.target?.result as string;
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    private applyEdgeDetection(): void {
        const imageData = this.ctx.getImageData(0, 0, this.canvas.width, this.canvas.height);
        const processedData = this.simulateEdgeDetection(imageData);
        this.ctx.putImageData(processedData, 0, 0);
        this.frameCount++;
    }

    private simulateEdgeDetection(imageData: ImageData): ImageData {
        const data = imageData.data;
        const width = imageData.width;
        const height = imageData.height;

        const output = new ImageData(width, height);
        const outputData = output.data;

        // Simple edge detection simulation
        for (let y = 1; y < height - 1; y++) {
            for (let x = 1; x < width - 1; x++) {
                const idx = (y * width + x) * 4;

                // Simple gradient calculation
                const brightness = this.getBrightness(data, idx);
                const rightBrightness = this.getBrightness(data, idx + 4);
                const bottomBrightness = this.getBrightness(data, idx + width * 4);

                const edgeValue = Math.abs(brightness - rightBrightness) +
                                 Math.abs(brightness - bottomBrightness);

                const value = Math.min(255, edgeValue * 2);
                outputData[idx] = value;
                outputData[idx + 1] = value;
                outputData[idx + 2] = value;
                outputData[idx + 3] = 255;
            }
        }

        return output;
    }

    private getBrightness(data: Uint8ClampedArray, index: number): number {
        return (data[index] + data[index + 1] + data[index + 2]) / 3;
    }

    private updateResolution(width: number, height: number): void {
        this.resolutionElement.textContent = `${width} × ${height}`;
    }

    private startFpsCounter(): void {
        setInterval(() => {
            const fps = this.frameCount;
            this.fpsElement.textContent = fps.toString();
            this.frameCount = 0;
        }, 1000);
    }
}

// Initialize when page loads
document.addEventListener('DOMContentLoaded', () => {
    new EdgeDetectionViewer();
});